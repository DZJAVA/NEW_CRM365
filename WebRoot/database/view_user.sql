select `u`.`id` AS `id`,`u`.`userName` AS `userName`,`d`.`id` AS `deptId`,`d`.`depaName` AS `deptName`,`d`.`superId` AS `superId` from (`dgg_user` `u` left join `dgg_department` `d` on((`u`.`department` = `d`.`id`)))