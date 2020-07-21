/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.test.mybatis.mapper;

import com.test.mybatis.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 数据访问层接口
 *
 * @author allen
 */
public interface UserMapper {

	// 插件式分页
	List<User> selectUsersByPage(Map<String, Object> map);

	// 根据id查询
	User selectUserById(User user);

	// 更新
	int updateUser(User user);


	// 借助数组进行分页
	List<User> selectUsersByArray();

	// 借助Sql语句进行分页
	List<User> selectUsersBySql(Map<String, Object> data);

	//public List<Article> selectArticleListPage(@Param("page") PageInfo page, @Param("userid") int userid);
}
