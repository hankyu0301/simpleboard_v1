package org.hankyu.simpleboard_v1.factory.entity;

import org.hankyu.simpleboard_v1.entity.member.Role;
import org.hankyu.simpleboard_v1.entity.member.RoleType;

public class RoleFactory {
    public static Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }
}
