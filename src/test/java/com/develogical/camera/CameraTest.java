package com.develogical.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {

         Sensor sensor = context.mock(Sensor.class);

         context.checking(new Expectations(){{
             exactly(1).of(sensor).powerUp();
         }});

         new Camera(sensor).powerOn();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        Sensor sensor = context.mock(Sensor.class);

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerDown();
        }});

        new Camera(sensor).powerOff();
    }

    @Test
    public void shutterWithPowerOn() {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard mc = context.mock(MemoryCard.class);
        byte[] data = new byte[] {123};

        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
                will (returnValue(data));
            exactly(1).of(mc).write(data);
        }});

        Camera camera = new Camera(sensor, mc);
        camera.powerOn();
        camera.pressShutter();
    }
    
    @Test
    public void pressingTheShutterWhenThePowerIsOffDoesNothing() {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard mc = context.mock(MemoryCard.class);
        byte[] data = new byte[] {123};

        context.checking(new Expectations(){{
            allowing(sensor).powerDown();
            exactly(0).of(sensor).readData();
            will (returnValue(data));
            exactly(0).of(mc).write(data);
        }});

        Camera camera = new Camera(sensor, mc);
        camera.powerOff();
        camera.pressShutter();
    }

    @Test
    public void powerOffWhenDataWrite() {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard mc = context.mock(MemoryCard.class);
        byte[] data = new byte[] {123};

        Camera camera = new Camera(sensor, mc);
        context.checking(new Expectations(){{
            exactly(1).of(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will (returnValue(data));
            exactly(1).of(mc).write(data);
            exactly(0).of(sensor).powerDown();
        }});

        camera.powerOn();
        camera.pressShutter();
        camera.powerOff();
    }

    @Test
    public void writeCompleteThanPowerDown() {
        Sensor sensor = context.mock(Sensor.class);
        MemoryCard mc = context.mock(MemoryCard.class);
        byte[] data = new byte[] {123};

        Camera camera = new Camera(sensor, mc);
        context.checking(new Expectations(){{
            allowing(sensor).powerUp();
            exactly(1).of(sensor).readData();
            will (returnValue(data));
            exactly(1).of(mc).write(data);
            exactly(1).of(sensor).powerDown();
        }});

        camera.powerOn();
        camera.pressShutter();
        camera.writeComplete();
    }
}

