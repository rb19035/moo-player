package org.tcgms.network.player.api.bo;

import java.nio.file.Path;
import java.util.List;

public record PlayerStatus(String currentMediaTitle, Path currentMediaFilePath,
                           List<Path> mediaPathQueue, MooPlayerMediaStatus currentMediaPlayStatus,
                           MooPlayerConfiguration playConfiguration, String groupName)
{}
