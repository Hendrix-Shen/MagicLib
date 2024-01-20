package top.hendrixshen.magiclib.api.platform;

import top.hendrixshen.magiclib.api.dependency.DistType;

public interface IPlatform {
    String getPlatformName();

    DistType getCurrentDistType();

    boolean matchesSide(DistType side);

    boolean isModLoaded(String modIdentifier);

    boolean isDevelopmentEnvironment();

    String getModName(String modIdentifier);


    String getModVersion(String modIdentifier);
}

