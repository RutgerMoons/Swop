package domain.assembly;

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
	
	public abstract String toString();
}
