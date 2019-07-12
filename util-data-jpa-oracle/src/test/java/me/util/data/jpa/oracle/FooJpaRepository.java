package me.util.data.jpa.oracle;

import me.util.data.jpa.CustomJpaRepository;

/**
 * File Name             :  MongoFooRepository
 * Author                :  sylar
 * Create Date           :  2018/4/15
 * Description           :
 * Reviewed By           :
 * Reviewed On           :
 * Version History       :
 * Modified By           :
 * Modified Date         :
 * Comments              :
 * CopyRight             : COPYRIGHT(c) xxx.com   All Rights Reserved
 * *******************************************************************************************
 */
public interface FooJpaRepository extends CustomJpaRepository<Foo, Long> {

    Foo getByName(String name);
}
