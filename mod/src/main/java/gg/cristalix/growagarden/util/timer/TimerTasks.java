package gg.cristalix.growagarden.util.timer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Timer;
import java.util.TimerTask;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimerTasks extends TimerTask {

  Runnable runnable;

  @Override
  public void run() {
    runnable.run();
  }

  public Timer start(String name, long delay, long period) {
    Timer timer = new Timer(name);
    timer.schedule(this, delay, period);
    return timer;
  }
}
