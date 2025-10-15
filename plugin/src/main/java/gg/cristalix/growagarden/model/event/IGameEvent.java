package gg.cristalix.growagarden.model.event;

public interface IGameEvent {
    void onStart();
    void onCancel();
    EventData getData();
}
