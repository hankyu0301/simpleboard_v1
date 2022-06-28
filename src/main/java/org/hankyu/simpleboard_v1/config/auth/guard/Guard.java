package org.hankyu.simpleboard_v1.config.auth.guard;

import org.hankyu.simpleboard_v1.entity.member.RoleType;

import java.util.List;
import java.util.Set;

public abstract class Guard {
    public final boolean check(Long id) {
        return hasRole(getRoleType()) || isResourceOwner(id);
    }
    abstract protected Set<RoleType> getRoleType();
    abstract protected boolean isResourceOwner(Long id);
    private boolean hasRole(Set<RoleType> roleTypes) {
        return roleTypes.stream().allMatch(roleType -> AuthHelper.extractMemberRoles().contains(roleType));
    }
}
