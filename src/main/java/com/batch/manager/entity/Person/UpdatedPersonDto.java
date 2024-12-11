package com.batch.manager.entity.Person;

public record UpdatedPersonDto(
        Long idPerson,
        String fullName,
        String codeDevice
) {
}
