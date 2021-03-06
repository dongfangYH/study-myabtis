package com.study.mybatis.framework.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {
    Object[] getParameterObjects();

    void setParameters(PreparedStatement ps)
            throws SQLException;

}
