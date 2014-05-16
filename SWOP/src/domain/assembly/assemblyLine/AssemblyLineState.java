package domain.assembly.assemblyLine;
/**
 * A class representing the state of an AssemblyLine. The state of an AssemblyLine can be operational, broken or maintenance.
 */
public enum AssemblyLineState {

	OPERATIONAL {
		@Override
		public String toString() {
			return "The assemblyLine is operational";
		}
	}, MAINTENANCE {
		@Override
		public String toString() {
			return "The assemblyLine is under maintenance";
		}
	}, BROKEN {
		@Override
		public String toString() {
			return "The assemblyLine is broken";
		}
	};
	
	@Override
	public abstract String toString();
}
