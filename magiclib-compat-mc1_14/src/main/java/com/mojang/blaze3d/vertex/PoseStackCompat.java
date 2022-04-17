package com.mojang.blaze3d.vertex;

import com.google.common.collect.Queues;
import com.mojang.math.Matrix3fCompat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import top.hendrixshen.magiclib.compat.annotation.Remap;
import top.hendrixshen.magiclib.compat.com.mojang.math.Matrix4fCompatApi;
import top.hendrixshen.magiclib.compat.util.MthCompatApi;

import java.util.Deque;

@Remap("net/minecraft/class_4587")
public class PoseStackCompat {
    private final Deque<Pose> poseStack = Queues.newArrayDeque();

    {
        Matrix4f matrix4f = new Matrix4f();
        ((Matrix4fCompatApi) matrix4f).setIdentityCompat();
        Matrix3fCompat matrix3f = new Matrix3fCompat();
        matrix3f.setIdentityCompat();
        poseStack.add(new Pose(matrix4f, matrix3f));
    }

    @Remap(value = "method_22904", dup = true)
    public void translateCompat(double d, double e, double f) {
        Pose pose = this.poseStack.getLast();
        ((Matrix4fCompatApi) pose.pose).multiplyWithTranslationCompat((float) d, (float) e, (float) f);
    }

    @Remap(value = "method_22905", dup = true)
    public void scaleCompat(float f, float g, float h) {
        Pose pose = this.poseStack.getLast();
        ((Matrix4fCompatApi) pose.pose).multiplyCompat(Matrix4fCompatApi.createScaleMatrixCompat(f, g, h));
        if (f == g && g == h) {
            if (f > 0.0F) {
                return;
            }
            pose.normal.mulCompat(-1.0F);
        }

        float i = 1.0F / f;
        float j = 1.0F / g;
        float k = 1.0F / h;
        float l = MthCompatApi.fastInvCubeRootCompat(i * j * k);
        pose.normal.mulCompat(Matrix3fCompat.createScaleMatrixCompat(l * i, l * j, l * k));
    }

    @Remap(value = "method_22907", dup = true)
    public void mulPoseCompat(Quaternion quaternion) {
        Pose pose = this.poseStack.getLast();
        ((Matrix4fCompatApi) pose.pose).multiplyCompat(quaternion);
        pose.normal.mulCompat(quaternion);
    }

    @Remap(value = "method_22903", dup = true)
    public void pushPoseCompat() {
        Pose pose = this.poseStack.getLast();
        this.poseStack.addLast(new Pose(((Matrix4fCompatApi) pose.pose).copyCompat(), pose.normal.copyCompat()));
    }

    @Remap(value = "method_22909", dup = true)
    public void popPoseCompat() {
        this.poseStack.removeLast();
    }

    @Remap(value = "method_23760", dup = true)
    public Pose lastCompat() {
        return this.poseStack.getLast();
    }


    @Remap(value = "method_22911", dup = true)
    public boolean clearCompat() {
        return this.poseStack.size() == 1;
    }

    @Remap(value = "method_34426", dup = true)
    public void setIdentityCompat() {
        Pose pose = this.poseStack.getLast();
        ((Matrix4fCompatApi) pose.pose).setIdentityCompat();
        pose.normal.setIdentityCompat();
    }

    @Remap(value = "method_34425", dup = true)
    public void mulPoseMatrixCompat(Matrix4f matrix4f) {
        ((Matrix4fCompatApi) this.poseStack.getLast().pose).multiplyCompat(matrix4f);
    }

    @Remap("net/minecraft/class_4587$class_4665")
    public static final class Pose {
        final Matrix4f pose;
        final Matrix3fCompat normal;

        Pose(Matrix4f matrix4f, Matrix3fCompat matrix3f) {
            this.pose = matrix4f;
            this.normal = matrix3f;
        }

        @Remap(value = "method_23761", dup = true)
        public Matrix4f poseCompat() {
            return this.pose;
        }

        @Remap(value = "method_23762", dup = true)
        public Matrix3fCompat normalCompat() {
            return this.normal;
        }
    }
}
