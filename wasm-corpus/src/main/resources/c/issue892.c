#include <stdint.h>

__attribute__((import_module("hedge")))
__attribute__((import_name("draw_rect")))
void draw_rect(float x, float y, float width, float height);

void run(int32_t pointer_pressed, float pointer_x, float pointer_y) {
    if (pointer_pressed == 1) {
      draw_rect(pointer_x, pointer_y, 32.0, 33.0);
    }
}
