# == Schema Information
#
# Table name: apps
#
#  id            :integer          not null, primary key
#  identifier    :string
#  version       :string
#  versionNumber :integer
#  description   :string
#  permissionStr :text
#  created_at    :datetime         not null
#  updated_at    :datetime         not null
#  name          :string
#  appUrl        :string
#  mobileUrl     :string
#

class App < ActiveRecord::Base
end
