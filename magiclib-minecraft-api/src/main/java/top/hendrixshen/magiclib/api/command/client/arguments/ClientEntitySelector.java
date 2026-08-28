package top.hendrixshen.magiclib.api.command.client.arguments;

import com.google.common.collect.Lists;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.selector.EntitySelector}.
 *
 * <p>Unlike the vanilla selector, which requires a {@code CommandSourceStack} to resolve entities,
 * this selector resolves them against a {@link ClientCommandSource} by iterating the entities of the
 * client level.</p>
 */
public class ClientEntitySelector {
    private final int maxResults;
    private final boolean includesEntities;
    private final boolean currentEntity;
    private final Predicate<Entity> predicate;
    private final String playerName;
    private final UUID entityUUID;

    /**
     * Creates a client entity selector.
     *
     * @param maxResults       the maximum number of results
     * @param includesEntities whether non-player entities are included
     * @param currentEntity    whether this selector refers to the executing entity
     * @param predicate        the entity predicate
     * @param playerName       the player name, or null
     * @param entityUUID       the entity UUID, or null
     */
    public ClientEntitySelector(
            int maxResults,
            boolean includesEntities,
            boolean currentEntity,
            Predicate<Entity> predicate,
            String playerName,
            UUID entityUUID
    ) {
        this.maxResults = maxResults;
        this.includesEntities = includesEntities;
        this.currentEntity = currentEntity;
        this.predicate = predicate;
        this.playerName = playerName;
        this.entityUUID = entityUUID;
    }

    /**
     * Gets the maximum number of results.
     *
     * @return the maximum number of results
     */
    public int getMaxResults() {
        return this.maxResults;
    }

    /**
     * Whether non-player entities are included.
     *
     * @return true if entities are included
     */
    public boolean includesEntities() {
        return this.includesEntities;
    }

    /**
     * Whether this selector refers to the executing entity.
     *
     * @return true if this is the self selector
     */
    public boolean isSelfSelector() {
        return this.currentEntity;
    }

    /**
     * Finds a single entity matching this selector.
     *
     * @param source the client command source
     * @return the matching entity
     */
    public Entity findSingleEntity(ClientCommandSource source) {
        List<? extends Entity> list = this.findEntities(source);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Finds all entities matching this selector.
     *
     * @param source the client command source
     * @return the matching entities
     */
    public List<? extends Entity> findEntities(ClientCommandSource source) {
        if (!this.includesEntities) {
            return this.findPlayers(source);
        }

        if (this.playerName != null) {
            for (Entity entity : source.getLevel().entitiesForRendering()) {
                if (entity instanceof AbstractClientPlayer
                        //#if MC >= 1.21.10
                        //$$ && ((AbstractClientPlayer) entity).getGameProfile().name().equals(this.playerName)
                        //#else
                        && ((AbstractClientPlayer) entity).getGameProfile().getName().equals(this.playerName)
                    //#endif
                ) {
                    return Lists.newArrayList(entity);
                }
            }

            return Collections.emptyList();
        }

        if (this.entityUUID != null) {
            for (Entity entity : source.getLevel().entitiesForRendering()) {
                if (entity.getUUID().equals(this.entityUUID)) {
                    return Lists.newArrayList(entity);
                }
            }

            return Collections.emptyList();
        }

        if (this.currentEntity) {
            Entity entity = source.getEntity();
            return entity != null && this.predicate.test(entity)
                    ? Lists.newArrayList(entity)
                    : Collections.emptyList();
        }

        List<Entity> list = Lists.newArrayList();

        for (Entity entity : source.getLevel().entitiesForRendering()) {
            if (this.predicate.test(entity)) {
                list.add(entity);

                if (list.size() >= this.maxResults) {
                    break;
                }
            }
        }

        return list;
    }

    /**
     * Finds a single player matching this selector.
     *
     * @param source the client command source
     * @return the matching player, or null
     */
    public AbstractClientPlayer findSinglePlayer(ClientCommandSource source) {
        List<AbstractClientPlayer> list = this.findPlayers(source);
        return list.size() == 1 ? list.get(0) : null;
    }

    /**
     * Finds all players matching this selector.
     *
     * @param source the client command source
     * @return the matching players
     */
    public List<AbstractClientPlayer> findPlayers(ClientCommandSource source) {
        if (this.playerName != null) {
            for (AbstractClientPlayer player : source.getLevel().players()) {
                if (
                        //#if MC >= 1.21.10
                        //$$ player.getGameProfile().name().equals(this.playerName)
                        //#else
                        player.getGameProfile().getName().equals(this.playerName)
                        //#endif
                ) {
                    return Lists.newArrayList(player);
                }
            }

            return Collections.emptyList();
        }

        if (this.entityUUID != null) {
            for (AbstractClientPlayer player : source.getLevel().players()) {
                if (player.getUUID().equals(this.entityUUID)) {
                    return Lists.newArrayList(player);
                }
            }

            return Collections.emptyList();
        }

        if (this.currentEntity) {
            Entity entity = source.getEntity();
            return entity instanceof AbstractClientPlayer && this.predicate.test(entity)
                    ? Lists.newArrayList((AbstractClientPlayer) entity)
                    : Collections.emptyList();
        }

        List<AbstractClientPlayer> list = Lists.newArrayList();

        for (AbstractClientPlayer player : source.getLevel().players()) {
            if (this.predicate.test(player)) {
                list.add(player);

                if (list.size() >= this.maxResults) {
                    break;
                }
            }
        }

        return list;
    }
}
