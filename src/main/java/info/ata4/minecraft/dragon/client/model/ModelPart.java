/*
 ** 2012 Februar 10
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.dragon.client.model;

import info.ata4.minecraft.dragon.util.math.MathX;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import static org.lwjgl.opengl.GL11.*;

/**
 * Extended model renderer with some helpful extra methods.
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ModelPart extends ModelRenderer {

    public static boolean renderAxes;

    public float renderScaleX = 1;
    public float renderScaleY = 1;
    public float renderScaleZ = 1;

    public float preRotateAngleX;
    public float preRotateAngleY;
    public float preRotateAngleZ;

    private ModelBase base;
    private boolean compiled;
    private int displayList;

    public ModelPart(ModelBase base, String name) {
        super(base, name);
        this.base = base;
    }

    public ModelPart(ModelBase base) {
        this(base, null);
    }

    public ModelPart(ModelBase modelbase, int i, int j) {
        super(modelbase, i, j);
        base = modelbase;
    }

    public ModelPart addChildBox(String name, float xOfs, float yOfs, float zOfs, int width, int length, int height) {
        ModelPart part = new ModelPart(base, boxName);
        part.mirror = mirror;
        part.addBox(name, xOfs, yOfs, zOfs, width, length, height);
        addChild(part);

        return part;
    }

    public ModelPart setAngles(float x, float y, float z) {
        rotateAngleX = x;
        rotateAngleY = y;
        rotateAngleZ = z;

        return this;
    }

    public ModelPart setRenderScale(float scaleX, float scaleY, float scaleZ) {
        this.renderScaleX = scaleX;
        this.renderScaleY = scaleY;
        this.renderScaleZ = scaleZ;

        return this;
    }

    public ModelPart setRenderScale(float scale) {
        return setRenderScale(scale, scale, scale);
    }

    private void compileDisplayList(float scale) {
        displayList = GLAllocation.generateDisplayLists(1);
        glNewList(displayList, GL_COMPILE);
        for (ModelBox obj : cubeList) {
            obj.render(Tessellator.instance, scale);
        }
        glEndList();
        compiled = true;
    }

    @Override
    public void render(float scale) {
        renderWithRotation(scale);
    }

    @Override
    public void renderWithRotation(float scale) {
        // skip if hidden
        if (isHidden || !showModel) {
            return;
        }

        // compile if required
        if (!compiled) {
            compileDisplayList(scale);
        }

        glPushMatrix();

        postRender(scale);

        // render axes
        //if (renderAxes) {
//            GLUtils.renderAxes(scale * 8);
        //}

        // call display list
        glCallList(displayList);

        // render child models
        if (childModels != null) {
            for (ModelRenderer obj : childModels) {
                obj.render(scale);
            }
        }

        glPopMatrix();
    }

    @Override
    public void postRender(float scale) {
        // skip if hidden
        if (isHidden || !showModel) {
            return;
        }

        // translate
        glTranslatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);

        // rotate
        if (preRotateAngleZ != 0) {
            glRotatef(MathX.toDegrees(preRotateAngleZ), 0, 0, 1);
        }
        if (preRotateAngleY != 0) {
            glRotatef(MathX.toDegrees(preRotateAngleY), 0, 1, 0);
        }
        if (preRotateAngleX != 0) {
            glRotatef(MathX.toDegrees(preRotateAngleX), 1, 0, 0);
        }

        if (rotateAngleZ != 0) {
            glRotatef(MathX.toDegrees(rotateAngleZ), 0, 0, 1);
        }
        if (rotateAngleY != 0) {
            glRotatef(MathX.toDegrees(rotateAngleY), 0, 1, 0);
        }
        if (rotateAngleX != 0) {
            glRotatef(MathX.toDegrees(rotateAngleX), 1, 0, 0);
        }

        // scale
        if (renderScaleX != 0 || renderScaleY != 0 || renderScaleZ != 0) {
            glScalef(renderScaleX, renderScaleY, renderScaleZ);
        }
    }
}
