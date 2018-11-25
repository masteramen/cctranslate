package cc.translate;

import dorkbox.tweenEngine.TweenEngine;

public class TestTween {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// We need a manager to handle every tween.

		TweenEngine manager = TweenEngine.create()
                .unsafe()  // access is only from a single thread ever, so unsafe is preferred.
                .build();
		// We can now create as many interpolations as we need !
		Particle particle1 = new Particle();
		manager.to(particle1, ParticleAccessor.POSITION_XY,new ParticleAccessor(), 1.0f)
		    .target(100, 200)
		    .start();
		manager.update(0);

		    

	}

}
