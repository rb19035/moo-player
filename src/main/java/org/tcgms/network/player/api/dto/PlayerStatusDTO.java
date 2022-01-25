package org.tcgms.network.player.api.dto;

import java.util.List;

public class PlayerStatusDTO
{
    private String currentMediaTitle;
    private String currentMediaURI;
    private List<String> mediaUIRQueue;
    private MooPlayerMediaStatus currentMediaPlayStatus;
    private MooPlayerConfiguration playConfiguration;

    public String getCurrentMediaTitle()
    {
        return currentMediaTitle;
    }

    public void setCurrentMediaTitle(String currentMediaTitle)
    {
        this.currentMediaTitle = currentMediaTitle;
    }

    public String getCurrentMediaURI()
    {
        return currentMediaURI;
    }

    public void setCurrentMediaURI(String currentMediaURI)
    {
        this.currentMediaURI = currentMediaURI;
    }

    public List<String> getMediaUIRQueue()
    {
        return mediaUIRQueue;
    }

    public void setMediaUIRQueue(List<String> mediaUIRQueue)
    {
        this.mediaUIRQueue = mediaUIRQueue;
    }

    public MooPlayerMediaStatus getCurrentMediaPlayStatus()
    {
        return currentMediaPlayStatus;
    }

    public void setCurrentMediaPlayStatus(MooPlayerMediaStatus currentMediaPlayStatus)
    {
        this.currentMediaPlayStatus = currentMediaPlayStatus;
    }

    public MooPlayerConfiguration getPlayConfiguration()
    {
        return playConfiguration;
    }

    public void setPlayConfiguration(MooPlayerConfiguration playConfiguration)
    {
        this.playConfiguration = playConfiguration;
    }
}
