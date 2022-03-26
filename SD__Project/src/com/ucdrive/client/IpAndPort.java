package com.ucdrive.client;

public class IpAndPort {
    private int commandPort, filePort;
    private String ip;

    public IpAndPort() {
        this.commandPort = -1;
        this.filePort = -1;
        this.ip = null;
    }
    public IpAndPort(int filePort){
        this.filePort = filePort;
    }
    public IpAndPort(String ip, int commandPort) {
        this.ip = ip;
        this.commandPort = commandPort;
    }

    // getters and setters
    public int getCommandPort() { return commandPort; }
    public void setCommandPort(int port) { this.commandPort = port; }

    public int getFilePort() { return filePort; }
    public void setFilePort(int filePort) { this.filePort = filePort; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    @Override
    public String toString() {
        return "IpAndPort{" +
                "commandPort=" + commandPort +
                ", filePort=" + filePort +
                ", ip='" + ip + '\'' +
                '}';
    }
}
