class RemoveDownloadsFromApp < ActiveRecord::Migration
  def change
    remove_column :apps, :downloads, :integer
  end
end
