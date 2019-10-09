package com.pointlion.sys.mvc.common.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.ICallback;
public class GenProIdService{  
	public String proId=null;
	public void runCountProc() {
        Object o = Db.execute(new ICallback() {
            @Override
            public Object call(Connection conn) throws SQLException {
                CallableStatement proc = conn.prepareCall("{? = call seq(?)}");
                proc.registerOutParameter(1, java.sql.Types.INTEGER);
                proc.setString(2, "proId");
                proc.execute();
                //代码来到这里就说明你的存储过程已经调用成功，如果有输出参数，接下来就是取输出参数的一个过程
                proId = proc.getString(1);
                //System.out.println(result);
                return proc;
            }
            
        });
    }
	
}  