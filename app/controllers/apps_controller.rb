class AppsController < ApplicationController
  def processApp(app)
        app["downloads"] = 0
        app["requires"] = []
        app["ratings"] = 0
        app["uploadedById"] = 0
        app["uploadedByName"] = ""
        app["permissions"] = app["permissionStr"].split(",").map(&:strip).reject(&:empty?)
        app["url"] = app["appUrl"]
        app.except("permissionStr").except("appUrl")
  end

  def index
    apps = App.all.to_a.map(&:serializable_hash)
    allApps = []
    apps.each{ |app| allApps << processApp(app) }
    paginate json: allApps, per_page: 20

    # bank = {"id" => 1,
    # 	    "identifier" => "eu.ownyourdata.bank",
    #         "version" => "1.0.0",
    #         "versionNumber" => 1,
    #         "downloads" => 1,
    #         "description" => "text",
    #         "requires" => [],
    #         "ratings" => -1.0,
    #         "uploadedById" => 1052,
    #         "uploadedByName" => "christoph",
    #         "permissions" => ["eu.ownyourdata.bank:read"]}
    # allergy = {"id" => 2,
    # 	       "identifier" => "eu.ownyourdata.allergy",
    #            "version" => "1.0.0",
    #            "versionNumber" => 1,
    #            "downloads" => 1,
    #            "description" => "text",
    #            "requires" => [],
	   #     "ratings" => -1.0,
	   #     "uploadedById" => 1052,
	   #     "uploadedByName" => "christoph",
    #            "permissions" => ["eu.ownyourdata.vaccrec:read","eu.ownyourdata.vaccrec:write"]}
    # data = [bank, allergy]
    # data.map{|i| data.id}

    # respond_to do |format|
    #   format.json {
    #     render :json => pData.to_json
    #   }
    # end
  end

  def show
    app = App.find(params[:id]).serializable_hash
    app = processApp(app)
    render json: app.to_json
  end
end
