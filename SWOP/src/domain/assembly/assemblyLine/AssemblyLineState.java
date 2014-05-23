package domain.assembly.assemblyLine;
/**
 * A class representing the state of an AssemblyLine. 
 * The state of an AssemblyLine can be operational, broken or maintenance.
 */
public enum AssemblyLineState {

	/**
	 * The AssemblyLine can process Vehicles.
	 */
	OPERATIONAL {
		@Override
		public String toString() {
			return "The assemblyLine is operational";
		}
	},
	/**
	 * The AssemblyLine is under maintenance, so no new Vehicles can be processed.
	 */
	MAINTENANCE {
		@Override
		public String toString() {
			return "The assemblyLine is under maintenance";
		}
	}, 
	/**
	 * The AssemblyLine is broken and needs to be fixed before it can process Vehicles.
	 * The Vehicles that are on the AssemblyLine are stuck at their WorkBenches.
	 */
	BROKEN {
		@Override
		public String toString() {
			return "The assemblyLine is broken";
		}
	}, 
	/**
	 * The AssemblyLine is idle and waiting for new Vehicles to arrive.
	 */
	IDLE {
		@Override
		public String toString() {
			return "The assemblyLine is operational (idle)";
		}
	}, 
	/**
	 * The AssemblyLine is finished for the day. It can process Vehicles the next day.
	 */
	FINISHED {
		@Override
		public String toString() {
			return "The assemblyLine is operational (finished)";
		}
	};
	
	@Override
	public abstract String toString();
}
