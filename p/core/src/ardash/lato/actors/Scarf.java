/*******************************************************************************
 * Copyright (C) 2020-2023 Andreas Redmer <ar-lato@abga.be>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ardash.lato.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.shape.Image3D;

public class Scarf extends Group3D {

	public static final float SEG_LEN = 0.3f;
	public static final float SEG_THICK = 0.1f;
	private float[] x = new float[20];
	private float[] y = new float[20];

	public Scarf(TextureRegion sTexture) {

		ModelBuilder mb = new ModelBuilder();
		setName("scarf");
		Color c = Color.RED.cpy();
		for (int i = 0; i < 20; i++) {
			Image3D img = new Image3D(SEG_LEN, SEG_THICK, sTexture, mb);
			addActor(img);
			c.add(0.1f, 0.01f, 0.02f, 0);
			img.setColor(c);
		}

	}

	@Override
	public void act(float delta) {
		super.act(delta);
		dragSegment(0, getX(), getY());
		for (int i = 0; i < x.length - 1; i++) {
			dragSegment(i + 1, x[i], y[i]);
		}
	}

	private void dragSegment(int i, float xin, float yin) {
		float dx = xin - x[i];
		float dy = yin - y[i];
		float angle = MathUtils.atan2(dy, dx);
		x[i] = xin - MathUtils.cos(angle) * SEG_LEN;
		y[i] = yin - MathUtils.sin(angle) * SEG_LEN;
		moveSegment(i, x[i], y[i], angle);
	}

	private void moveSegment(int i, float x, float y, float a) {
		a *= MathUtils.radiansToDegrees;
		final Actor3D ch = getChild(i);
		ch.setPosition(x, y);
		ch.setRotation(a);
	}

	@Override
	public void setPosition(float x, float y) {
		getChild(0).setPosition(x, y);
	}

	@Override
	public float getX() {
		return getChild(0).getX();
	}

	@Override
	public float getY() {
		return getChild(0).getY();
	}
}
