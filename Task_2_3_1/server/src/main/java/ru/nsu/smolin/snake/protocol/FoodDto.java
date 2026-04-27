package ru.nsu.smolin.snake.protocol;

/**
 * Единица еды на поле. {@code expiresAtTick == -1} означает постоянную еду.
 */
public class FoodDto {
    public int x, y;
    public long expiresAtTick;

    public FoodDto() {}

    public FoodDto(int x, int y, long expiresAtTick) {
        this.x = x;
        this.y = y;
        this.expiresAtTick = expiresAtTick;
    }
}
