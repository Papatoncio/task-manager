package com.papatoncio.taskapi.dto.organization;

import com.papatoncio.taskapi.common.PermissionLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Builder
public class OrganizationWithPermissionsResponse {
    private OrganizationResponse organization;
    private PermissionLevel permission;
}
