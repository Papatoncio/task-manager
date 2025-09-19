package com.papatoncio.taskapi.utils;

import com.papatoncio.taskapi.entities.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class MapperUtils {
    // Convierte Set<Role> a Set<String>
    public static Set<String> rolesToString(Set<Role> roles) {
        return roles.stream()
                .map(r -> r.getName().name()) // ajusta según tu entidad Role
                .collect(Collectors.toSet());
    }
}
