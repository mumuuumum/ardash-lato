package ardash.lato.actors;

import java.util.EnumSet;

public enum PlayerState implements HasNextStates {

	INIT {
		public EnumSet<PlayerState> nexts() {
			EnumSet<PlayerState> ret = EnumSet.of(SLIDING);
			return ret;
		}
	},
	SLIDING {
		public EnumSet<PlayerState> nexts() {
			EnumSet<PlayerState> ret = EnumSet.of(DUCKING, INAIR, CRASHED);
			return ret;
		}
	},
	DUCKING { // pretty much same as sliding
		public EnumSet<PlayerState> nexts() {
			EnumSet<PlayerState> ret = EnumSet.of(SLIDING, INAIR, CRASHED);
			return ret;
		}
	},
	INAIR { // JUMPING and snapped into air
		public EnumSet<PlayerState> nexts() {
			EnumSet<PlayerState> ret = EnumSet.of(SLIDING, DUCKING, ROLLING, GRINDING);
			return ret;
		}
	},
	ROLLING {
		public EnumSet<PlayerState> nexts() {
			EnumSet<PlayerState> ret = EnumSet.of(INAIR);
			return ret;
		}
	},
	GRINDING {
		public EnumSet<PlayerState> nexts() {
			EnumSet<PlayerState> ret = EnumSet.of(INAIR);
			return ret;
		}
	},
	CRASHED {
		public EnumSet<PlayerState> nexts() {
			EnumSet<PlayerState> ret = EnumSet.of(INIT);
			return ret;
		}
	};
	
	public boolean isInAir() {
		final EnumSet<PlayerState> inAir = EnumSet.of(PlayerState.INAIR, PlayerState.ROLLING);
		final boolean isInAir = inAir.contains(this);
		return isInAir;
	}

	/**
	 * moves to a new state and checks if it is valid
	 * @param newState
	 * @return
	 */
	public PlayerState moveTo(PlayerState newState) {
		if (newState.equals(this))
			return this;
		if (! this.nexts().contains(newState))
			throw new RuntimeException("invalid sate transfer from "+this+" to "+newState);

		return newState;
	}

	public boolean isStarted() {
		return ! this.equals(INIT);
	}
}
