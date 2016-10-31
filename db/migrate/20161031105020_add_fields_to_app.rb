class AddFieldsToApp < ActiveRecord::Migration
  def change
    add_column :apps, :name, :string
    add_column :apps, :appUrl, :string
    add_column :apps, :mobileUrl, :string
  end
end
