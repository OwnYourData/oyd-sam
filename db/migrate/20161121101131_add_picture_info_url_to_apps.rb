class AddPictureInfoUrlToApps < ActiveRecord::Migration
  def change
    add_column :apps, :picture, :string
    add_column :apps, :infoUrl, :string
  end
end
