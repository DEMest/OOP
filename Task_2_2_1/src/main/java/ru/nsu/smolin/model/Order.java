package ru.nsu.smolin.model;

public class Order {
    private final int id;
    private volatile OrderState state = OrderState.PENDING;
    private Integer bakerId;
    private Integer courierId;

    public Order(int id) {
        this.id = id;
    }

    public void setState(OrderState state) { this.state = state; }
    public void setBakerId(int bakerId) { this.bakerId = bakerId; }
    public void setCourierId(int courierId) { this.courierId = courierId; }

    public int getId() { return id; }
    public OrderState getState() { return state; }
    public Integer getBakerId() { return bakerId; }
    public Integer getCourierId() { return courierId; }

    @Override
    public String toString() {
        return String.format("[%3d] %s", id, state.getDisplay());
    }
}