class AppsController < ApplicationController
  def processApp(app)
        app["downloads"] = 0
        app["requires"] = []
        app["ratings"] = 0
        app["uploadedById"] = 0
        app["uploadedByName"] = ""
        app["permissions"] = app["permissionStr"].split(",").map(&:strip).reject(&:empty?)
        app["url"] = app["appUrl"]
        app["infourl"] = app["infoUrl"]
        app.except("permissionStr").except("appUrl").except("infoUrl")
  end

  def index
    apps = App.all.to_a.map(&:serializable_hash)
    allApps = []
    apps.each{ |app| allApps << processApp(app) }
    paginate json: allApps, per_page: 20
  end

  def show
    app = App.find(params[:id]).serializable_hash
    app = processApp(app)
    render json: app.to_json
  end
end
