package me.scherbs.autominer.tier;

public class Tier {

  private TierType type;
  private Long time;
  private boolean paused;

  public Tier(TierType type, Long time, boolean paused) {
    this.type = type;
    this.time = time;

    this.setPaused(paused);
  }

  public Tier(TierType tierType) {
    this.type = tierType;
    this.time = System.currentTimeMillis();
    this.setPaused(true);
  }

  public TierType getType() {
    return type;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public void addTime(Long time) {
    this.time = this.time + time;
  }

  public boolean isPaused() {
    return paused;
  }

  public long getRemaining() {
    long value = time;
    if (!paused) {
      value-=System.currentTimeMillis();
    }
    return value;
  }

  public void setPaused(boolean paused) {
    if (paused != this.paused) time = paused ? time - System.currentTimeMillis() : time + System.currentTimeMillis();
    this.paused = paused;
  }
}
