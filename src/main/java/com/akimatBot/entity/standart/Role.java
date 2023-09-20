package com.akimatBot.entity.standart;

import com.akimatBot.web.dto.RoleDTO;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Role {
    String name;
    String description;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public Role(long id) {
        this.id = id;
    }
    public Role() {
    }

    public RoleDTO getDTO() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(this.id);
        roleDTO.setName(this.name);
        return roleDTO;
    }
}
