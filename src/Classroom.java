public class Classroom {

    // minimum amount of seats for a classroom to handle ClassroomTooSmallException
    public static final int MINIMUM_SEATS_REQUIREMENT = 30;
    // building name
    private String building;
    // room number
    private String roomNumber;
    // maximum amount of seats in a classroom
    private int seats;

    // constructor
    public Classroom(String building, String roomNumber, int seats) throws ClassroomTooSmallException {
        setBuilding(building);
        setRoomNumber(roomNumber);
        setSeats(seats);
    }

    // building name getter
    public String getBuilding() {
        return this.building;
    }

    // room number getter
    public String getRoomNumber() {
        return this.roomNumber;
    }

    // maximum amount of seats getter
    public int getSeats() {
        return this.seats;
    }

    // building name setter
    public void setBuilding(String buildingName) {
        if (buildingName == null) {
            throw new NullPointerException("building name cannot be null!");
        }
        if (buildingName.isBlank()) {
            throw new IllegalArgumentException("building name cannot be empty!");
        }
        this.building = buildingName.trim();
    }

    // room number setter
    public void setRoomNumber(String roomNumber) {
        if (roomNumber == null) {
            throw new NullPointerException("room number cannot be null!");
        }
        if (roomNumber.isBlank()) {
            throw new IllegalArgumentException("room number cannot be empty!");
        }
        this.roomNumber = roomNumber.trim();
    }

    // maximum amount of seats setter
    public void setSeats(int numberOfSeats) throws ClassroomTooSmallException {
        if (numberOfSeats <= 0) {
            throw new IllegalArgumentException("number of seats in a classroom must be a positive integer!");
        }
        // the assignment did not specify how to handle the exceptions so we propagate them back to Main
        if (numberOfSeats < MINIMUM_SEATS_REQUIREMENT) {
            throw new ClassroomTooSmallException("classroom must have at least " + MINIMUM_SEATS_REQUIREMENT + " seats!");
        }
        this.seats = numberOfSeats;
    }

    // classroom is defined by its physical location, so only the building name and room number determine equality
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (o instanceof Classroom) {
            Classroom other = (Classroom) o;
            return getBuilding().equals(other.getBuilding())
                    && getRoomNumber().equals(other.getRoomNumber());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "building='" + getBuilding() + '\'' +
                ", roomNumber='" + getRoomNumber() + '\'' +
                ", seats=" + getSeats() +
                '}';
    }
}
