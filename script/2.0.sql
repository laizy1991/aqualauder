USE `aqualauder`;

CREATE TABLE `administrator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '0-有效，1-已删除',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `modify_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='管理员账号表';

CREATE TABLE `audit_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `audit_type` int(11) NOT NULL COMMENT '审核类型，0-分销商身份审核',
  `content` varchar(500) CHARACTER SET gbk DEFAULT NULL,
  `img_urls` varchar(1024) CHARACTER SET gbk DEFAULT '' COMMENT '图片url，多个url以分号分割',
  `audit_status` tinyint(4) DEFAULT '0' COMMENT '审核状态，0为未审核，1为审核通过，-1为审核不通过',
  `create_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='审核信息表';

CREATE TABLE `cash_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `cash_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '提现方式: 0=红包,1=银行卡',
  `amount` int(11) NOT NULL COMMENT '金额，单位分',
  `cash_status` int(11) NOT NULL DEFAULT '0' COMMENT '状态：0-申请中，1-转账中，2-提现成功，3-提现失败',
  `slip_no` varchar(50) DEFAULT '0' COMMENT '凭证号',
  `mch_billno` varchar(50) DEFAULT '' COMMENT '商户发送红包订单号',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `mch_billno` (`mch_billno`),
  KEY `idx1` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='提现表';


CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `province_id` int(11) DEFAULT NULL,
  `city_sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=372 DEFAULT CHARSET=utf8 COMMENT='城市';


CREATE TABLE `common_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_type` int(11) NOT NULL COMMENT '字典类型',
  `dict_key` varchar(50) NOT NULL COMMENT '键',
  `dict_value` varchar(50) DEFAULT NULL COMMENT '值',
  `dict_desc` varchar(50) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint(4) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='通用字典表,用于保存一些字典值，包括配置等';


CREATE TABLE `distributor` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `distributor_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '分销商类型，0-个人',
  `distributor_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '分销商状态，0-为认证，1-认证通过，-1-认证不通过',
  `real_name` varchar(16) DEFAULT NULL COMMENT '真实姓名',
  `join_time` bigint(20) DEFAULT '0' COMMENT '成为分销商的时间',
  `link` varchar(500) DEFAULT NULL COMMENT '推广链接',
  `qrcode_tmp_wx_url` varchar(500) DEFAULT '' COMMENT '临时二维码微信url',
  `qrcode_tmp_path` varchar(300) DEFAULT '' COMMENT '临时二维码本地相对路径',
  `qrcode_tmp_ticket` varchar(300) DEFAULT '' COMMENT '临时二维码ticket',
  `qrcode_tmp_expire_time` bigint(20) DEFAULT '0' COMMENT '临时二维码过期时间',
  `qrcode_limit_wx_url` varchar(500) DEFAULT '' COMMENT '永久二维码微信url',
  `qrcode_limit_path` varchar(300) DEFAULT '' COMMENT '永久二维码本地相对路径',
  `qrcode_limit_ticket` varchar(300) DEFAULT '' COMMENT '永久二维码ticket',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分销商信息表';


CREATE TABLE `distributor_superior` (
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户id',
  `superior` bigint(20) DEFAULT '0' COMMENT '上线用户id',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  KEY `idx1` (`superior`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分销商上线表';


CREATE TABLE `district` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `city_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2825 DEFAULT CHARSET=utf8 COMMENT='区';


CREATE TABLE `express` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `name` varchar(50) NOT NULL COMMENT '快递公司名称',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='快递公司';


CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品唯一ID',
  `title` varchar(128) NOT NULL COMMENT '商品标题',
  `goods_type` int(11) NOT NULL DEFAULT '0',
  `goods_desc` varchar(512) NOT NULL DEFAULT '' COMMENT '商品详细描述',
  `price` int(11) NOT NULL COMMENT '商品原价：单位：分',
  `warm_prompt` text COMMENT '温馨提示',
  `state` tinyint(4) NOT NULL DEFAULT '1' COMMENT '商品状态，1上架，0下架',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx1` (`goods_type`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='商品';


CREATE TABLE `goods_color` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `name` varchar(50) NOT NULL COMMENT '颜色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='商品颜色';


CREATE TABLE `goods_icon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `goods_id` bigint(20) NOT NULL DEFAULT '0',
  `icon_url` varchar(512) NOT NULL DEFAULT '' COMMENT '图片地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='商品图片';


CREATE TABLE `goods_size` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `name` varchar(50) NOT NULL COMMENT '码数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='商品码数';


CREATE TABLE `goods_spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_id` bigint(20) NOT NULL DEFAULT '0',
  `spec_type` int(11) NOT NULL COMMENT '属性类别',
  `spec_info_id` int(11) NOT NULL COMMENT '属性值',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品属性';


CREATE TABLE `goods_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NOT NULL DEFAULT '0',
  `goods_size` int(11) NOT NULL DEFAULT '0',
  `goods_color` int(11) NOT NULL DEFAULT '0' COMMENT '商品颜色，-1表示所有颜色',
  `stock_desc` varchar(500) DEFAULT NULL COMMENT '库存描述',
  `amount` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='库存';


CREATE TABLE `goods_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `name` varchar(128) NOT NULL COMMENT '类型名字',
  `type_desc` varchar(512) NOT NULL DEFAULT '' COMMENT '类型描述',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父类型',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品分类';


CREATE TABLE `order` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `out_trade_no` varchar(255) NOT NULL DEFAULT '' COMMENT '订单编号',
  `pay_type` tinyint(4) DEFAULT NULL COMMENT '支付类型，0-微信支付，1-余额',
  `express_name` varchar(50) DEFAULT NULL COMMENT '快递公司',
  `express_num` varchar(255) DEFAULT NULL COMMENT '快递单号',
  `total_fee` int(11) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '订单对应的状态: 0=创建订单(待支付)，1=完成支付,2=待发货，3=完成发货，4=确认收货,5=交易成功(不可退货),6=交易关闭',
  `forbid_refund` tinyint(4) NOT NULL DEFAULT '0' COMMENT '默认0=允许退款，1=禁止退款-某些场景不允许退款操作',
  `order_memo` varchar(255) DEFAULT '' COMMENT '用户下单备注',
  `receiver` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `mobile_phone` varchar(11) DEFAULT '' COMMENT '收货人电话',
  `shipping_address` varchar(500) DEFAULT NULL COMMENT '收货地址',
  `state_history` varchar(2000) DEFAULT NULL,
  `deliver_time` bigint(20) DEFAULT '0' COMMENT '发货时间',
  `recev_time` bigint(20) DEFAULT '0' COMMENT '自动收货时间',
  `finish_time` bigint(20) DEFAULT '0' COMMENT '完成时间',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `open_id` varchar(255) DEFAULT '' COMMENT '用户公众号openId',
  `client_ip` varchar(20) DEFAULT '' COMMENT '来访IP',
  `callback_url` varchar(500) DEFAULT '' COMMENT '回调地址',
  `pay_status` tinyint(4) DEFAULT '1' COMMENT '前台回调状态 10初始状态 20准备支付 30放弃支付 40支付失败 50支付成功',
  `pay_time` bigint(20) DEFAULT '0' COMMENT '前台回调时间',
  `callback_status` tinyint(4) DEFAULT NULL COMMENT '支付回调状态 10初始状态 20回调失败 30回调成功',
  `callback_time` bigint(20) DEFAULT '0' COMMENT '支付回调时间',
  `platform_trade_no` varchar(128) DEFAULT '' COMMENT '支付平台订单号',
  `platform_trade_msg` varchar(500) DEFAULT '' COMMENT '支付平台返回信息',
  `platform_transation_id` varchar(50) DEFAULT '' COMMENT '支付平台交易单号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `out_trade_no` (`out_trade_no`),
  KEY `idx1` (`user_id`,`state`) USING BTREE,
  KEY `idx2` (`state`) USING BTREE,
  KEY `idx_transationId` (`platform_transation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单';


CREATE TABLE `order_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `goods_type` int(11) NOT NULL DEFAULT '0' COMMENT '商品类型：1=物品，2=赠品',
  `goods_title` varchar(500) NOT NULL DEFAULT '' COMMENT '商品标题',
  `goods_size` varchar(8) NOT NULL,
  `goods_desc` varchar(1000) DEFAULT '' COMMENT '商品描述',
  `goods_origin_price` int(11) DEFAULT '0' COMMENT '商品原价-单位分',
  `goods_discount_price` int(11) DEFAULT '0' COMMENT '商品打折价-单位分',
  `goods_discount` varchar(255) DEFAULT '' COMMENT '折扣描述',
  `goods_icon` varchar(1000) DEFAULT '' COMMENT '商品图片url',
  `goods_number` int(11) DEFAULT '1' COMMENT '购买商品的数量',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `goods_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx1` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='订单物品';


CREATE TABLE `province` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `pro_sort` int(11) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='省份';


CREATE TABLE `qrcode_share` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT '' COMMENT '名称',
  `friend_share_title` varchar(500) DEFAULT '' COMMENT '分享给好友标题',
  `friend_share_desc` varchar(500) DEFAULT '' COMMENT '分享给好友描述',
  `moment_share_title` varchar(500) DEFAULT '' COMMENT '分享到朋友圈标题',
  `img_path` varchar(500) DEFAULT '' COMMENT '图片本地路径',
  `img_url` varchar(500) DEFAULT '' COMMENT '图片链接',
  `is_enabled` tinyint(2) DEFAULT '0' COMMENT '是否启用',
  `remark` varchar(100) DEFAULT '' COMMENT '备注',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


CREATE TABLE `refund_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `order_id` bigint(20) NOT NULL COMMENT '对应的订单',
  `refund_type` tinyint(4) DEFAULT NULL COMMENT '退款类型(对应于order的pay_type)，0-微信支付，1-余额',
  `transaction_id` varchar(50) DEFAULT '' COMMENT '支付平台订单号，对应order表transaction_id',
  `out_trade_no` varchar(50) DEFAULT '' COMMENT '商户平台订单号，对应order表out_trade_no',
  `out_refund_no` varchar(50) DEFAULT NULL COMMENT '商户退款单号，传给支付平台，少于32位',
  `refund_id` varchar(50) DEFAULT '' COMMENT '支付平台退款单号',
  `refund_state` int(11) NOT NULL DEFAULT '0' COMMENT '退款状态: -1=无退款，0=申请退款,1=退款中,2=退款成功,3=拒绝,4=取消,5=退款失败,6=状态未确定,7=代入转发',
  `refund_recv_accout` varchar(100) DEFAULT '' COMMENT '退款入账方，只在订单用微信支付有效, 1)银行卡：{银行名称}{卡类型}{卡尾号} 2)支付用户零钱',
  `user_memo` varchar(255) DEFAULT '' COMMENT '用户退单备注',
  `seller_memo` varchar(255) DEFAULT '' COMMENT '商家备注，一般拒绝退款时使用',
  `state_history` varchar(2000) DEFAULT NULL,
  `op_user_id` bigint(20) DEFAULT NULL COMMENT '操作员user_id',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx1` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='退款单';


CREATE TABLE `sale_info` (
  `id` bigint(20) NOT NULL COMMENT '唯一ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品id，-1表示所有商品',
  `sale` int(11) NOT NULL COMMENT '折扣',
  `discount_desc` varchar(50) DEFAULT NULL COMMENT '打折的描述语',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除:0=否,1=是',
  `begin_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品折扣';


CREATE TABLE `shipping_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `name` varchar(50) NOT NULL COMMENT '收货人姓名',
  `user_id` int(11) NOT NULL,
  `mobile` varchar(50) NOT NULL COMMENT '电话',
  `district_id` int(11) NOT NULL,
  `postal` int(11) NOT NULL COMMENT '邮编',
  `address` varchar(500) NOT NULL COMMENT '详细地址',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='收货地址';


CREATE TABLE `spec_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(128) NOT NULL COMMENT '名字',
  `spec_type` int(11) NOT NULL COMMENT '属性类别',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='属性信息';


CREATE TABLE `spec_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(128) NOT NULL COMMENT '名字',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `spec_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='属性类别';


CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `mobile` varchar(11) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户手机号',
  `reg_type` int(3) NOT NULL DEFAULT '1' COMMENT '1 = 微信',
  `open_id` varchar(255) CHARACTER SET utf8 DEFAULT '' COMMENT '第三方平台ID',
  `nickname` varchar(50) DEFAULT '' COMMENT '昵称',
  `sex` tinyint(4) DEFAULT '0' COMMENT '0未知 1男性 2女性',
  `birthday` date DEFAULT NULL,
  `headimg_url` varchar(500) CHARACTER SET utf8 DEFAULT '' COMMENT '头像地址',
  `subscribe_time` bigint(20) DEFAULT '0' COMMENT '关注时间',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `open_id` (`open_id`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=10033 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息';


CREATE TABLE `user_id_pool` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=67168 DEFAULT CHARSET=utf8 COMMENT='账号资源池';


CREATE TABLE `user_month_blotters` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `blotter_month` int(11) DEFAULT '0' COMMENT '流水月份,形如200509',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  `month_blotters` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx1` (`user_id`,`blotter_month`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户月流水';


CREATE TABLE `user_wallet` (
  `user_id` bigint(20) NOT NULL,
  `balances` int(11) NOT NULL DEFAULT '0' COMMENT '余额',
  `card_no` varchar(50) DEFAULT NULL COMMENT '银行卡号',
  `bank_name` varchar(50) DEFAULT NULL,
  `income` int(11) NOT NULL DEFAULT '0' COMMENT '累计收入',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户钱包';


CREATE TABLE `user_wallet_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `oper_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '操作类型，0-转入，1-转出',
  `bill_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型，0-推广提成，1-支付，2-退款，3-提现，4-提现失败返回',
  `bill_month` int(11) DEFAULT '0' COMMENT '月份,形如201603',
  `trigger` int(11) DEFAULT '0' COMMENT '触发者，比如推广提成，记录的是下线用户id，没有则保存0',
  `amount` int(11) DEFAULT '0' COMMENT '变动金额',
  `balance` int(11) DEFAULT '0' COMMENT '余额',
  `obj_id` varchar(255) DEFAULT '' COMMENT '关联的其他表id，比如支付，对应的为订单',
  `bill_desc` varchar(500) DEFAULT '0' COMMENT '描述',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `bill_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx1` (`user_id`,`bill_month`,`bill_type`) USING BTREE,
  KEY `idx2` (`bill_month`,`bill_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户钱包清单';


CREATE TABLE `album` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `path` varchar(200) NOT NULL COMMENT 'path',
  `deleted` tinyint(4) DEFAULT '0' COMMENT '0-有效，1-已删除',
  `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='相册';
