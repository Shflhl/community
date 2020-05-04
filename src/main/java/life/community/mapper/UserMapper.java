package life.community.mapper;

import life.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountIdn},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
