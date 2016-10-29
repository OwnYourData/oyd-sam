class CreateApps < ActiveRecord::Migration
  def change
    create_table :apps do |t|
      t.string :identifier
      t.string :version
      t.integer :versionNumber
      t.integer :downloads
      t.string :description
      t.text :requires
      t.integer :ratings
      t.integer :uploadedById
      t.string :uploadedByName
      t.text :permissions

      t.timestamps null: false
    end
  end
end
