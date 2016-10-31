class RemoveFieldsFromApp < ActiveRecord::Migration
  def change
    remove_column :apps, :requires, :text
    remove_column :apps, :ratings, :integer
    remove_column :apps, :uploadedById, :integer
    remove_column :apps, :uploadedByName, :string
  end
end
