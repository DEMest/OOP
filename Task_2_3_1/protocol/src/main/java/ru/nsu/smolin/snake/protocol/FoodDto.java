package ru.nsu.smolin.snake.protocol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Единица еды на поле. {@code expiresAtTick == -1} означает постоянную еду.
 */
@Getter
@Setter
@NoArgsConstructor
public class FoodDto {
    private int x;
    private int y;
    private long expiresAtTick;

    public FoodDto(int x, int y, long expiresAtTick) {
        this.x = x;
        this.y = y;
        this.expiresAtTick = expiresAtTick;
    }
}
