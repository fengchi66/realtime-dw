package com.gmall.data.common.entity.dim

class DimUserInfo extends Serializable {

  var userBirthday: String = _
  var userGender: String = _
  var userLoginName: String= _

  override def toString = s"DimUserInfo(user_age=$userBirthday, user_gender=$userGender)"

}
