package com.batch.manager.config;

import com.batch.manager.entity.Person.Person;
import com.batch.manager.entity.deviceTracker.DeviceDto;
import com.batch.manager.entity.deviceTracker.DeviceTracker;
import com.batch.manager.repository.PersonRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ItemDeviceTrackerProcessor implements ItemProcessor<DeviceDto, DeviceTracker> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Getter
    private  List<Person> personSet;

    @Autowired
    private PersonRepository personRepository;

    @PostConstruct
    public void setPersonInit(){
        this.personSet = personRepository.findAll();
    }

    @Override
    public DeviceTracker process(DeviceDto item){
        DeviceTracker deviceTracker = new DeviceTracker();
        Person person = this.convertToPerson(item.getName(), personSet);
        deviceTracker.setPersonDeviceTracker(person);
        deviceTracker.setCreatedAtDeviceTracker(LocalDateTime.parse(item.getRegistrationDate().replace(" ", "T"), formatter));
        deviceTracker.setLatitude(this.latitudeValidate(item.getLatitude()));
        deviceTracker.setLongitude(this.longitudeValidate(item.getLongitude()));
        deviceTracker.setIdTextDeviceTracker(item.getIdText());
        this.validateDeviceTracker(deviceTracker);

        return deviceTracker;
    }

    private void validateDeviceTracker(DeviceTracker pDeviceTracker) {
        if(pDeviceTracker == null ||
                pDeviceTracker.getIdTextDeviceTracker() == null ||
                pDeviceTracker.getIdTextDeviceTracker().isEmpty()||
                pDeviceTracker.getCreatedAtDeviceTracker() == null||
                pDeviceTracker.getLatitude() == null||
                pDeviceTracker.getLongitude() == null){
            throw new IllegalArgumentException("Some field is null or empty.");
        }
    }

    protected Person convertToPerson(String pPersonName, List<Person> pPersonSet) {
        System.out.println(pPersonName);
        Person personConvert = pPersonSet.stream()
                .filter(p -> p.getFullName().equals(pPersonName))
                .findFirst()
                .orElse(null);
        if(personConvert == null) {
            throw new RuntimeException("Person "+pPersonName+" not found.");
        }
        return personConvert;
    }

    protected Double latitudeValidate(Double pLatitude){
        if(pLatitude == null) {
            throw new RuntimeException("Latitude is null");
        }
        Double MIN_LATITUDE = -90.0;
        Double MAX_LATITUDE = 90.0;
        if (pLatitude.compareTo(MIN_LATITUDE) >= 0 && pLatitude.compareTo(MAX_LATITUDE) <= 0){
            return pLatitude;
        }
        throw new RuntimeException("Latitude must be between -90 and 90.");
    }

    protected Double longitudeValidate(Double pLongitude){
        if(pLongitude == null) {
            throw new RuntimeException("Longitude is null");
        }
        Double MIN_LONGITUDE =  -180.0;
        Double MAX_LONGITUDE = 180.0;
        if (pLongitude.compareTo(MIN_LONGITUDE) >= 0 && pLongitude.compareTo(MAX_LONGITUDE) <= 0){
            return pLongitude;
        }
        throw new RuntimeException("Longitude must be between -180 and 180.");
    }
}
