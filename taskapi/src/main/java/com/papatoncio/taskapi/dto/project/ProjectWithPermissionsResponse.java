package com.papatoncio.taskapi.dto.project;

import com.papatoncio.taskapi.common.PermissionLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Builder
public class ProjectWithPermissionsResponse {
    private ProjectResponse project;
    private PermissionLevel permission;
}
