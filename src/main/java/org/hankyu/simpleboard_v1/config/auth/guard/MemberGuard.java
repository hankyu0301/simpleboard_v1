package org.hankyu.simpleboard_v1.config.auth.guard;

import lombok.RequiredArgsConstructor;
import org.hankyu.simpleboard_v1.entity.member.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MemberGuard extends Guard {

    private Set<RoleType> roleTypes = Set.of(RoleType.ROLE_ADMIN);

    @Override
    protected Set<RoleType> getRoleType() {

        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return id.equals(AuthHelper.extractMemberId());
    }
}
