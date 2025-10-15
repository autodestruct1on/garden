package gg.cristalix.growagarden.module;

import gg.cristalix.growagarden.channel.ChannelHandler;

public interface IModuleChannel extends IModule {
  void registerChannels(ChannelHandler channelHandler);
}
