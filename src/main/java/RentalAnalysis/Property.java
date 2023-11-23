package RentalAnalysis;

public class Property {
    private String name;
    private String location;
    private int bedrooms;
    private double price;

    public Property(String name, String location, int bedrooms, double price) {
        this.name = name;
        this.location = location;
        this.bedrooms = bedrooms;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", bedrooms=" + bedrooms +
                ", price=" + price +
                '}';
    }
}
