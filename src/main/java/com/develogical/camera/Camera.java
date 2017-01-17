package com.develogical.camera;

public class Camera implements WriteListener{
    Sensor sensor;
    MemoryCard mc;
    Boolean power, mcISWriting;

    public Camera(Sensor sensor, MemoryCard mc) {
        this.sensor = sensor;
        this.mc = mc;
        this.power = false;
        this.mcISWriting = false;
    }

    public Camera(Sensor sensor) {
        this.sensor = sensor;
        this.power = false;
        this.mcISWriting = false;
    }

    public void pressShutter() {
        if (power) {
            mcISWriting = true;
            mc.write(sensor.readData());
        }
    }

    public void powerOn() {
        sensor.powerUp();
        power = true;
    }

    public void powerOff() {
        if(!mcISWriting) {
            sensor.powerDown();
            power = false;
        }
    }

    @Override
    public void writeComplete() {
        mcISWriting = false;
        sensor.powerDown();
    }
}

