class ChangePermissionNameInApps < ActiveRecord::Migration
  def change
    rename_column :apps, :permissions, :permissionStr
  end
end
