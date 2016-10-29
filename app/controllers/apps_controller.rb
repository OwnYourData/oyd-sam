class AppsController < ApplicationController
  def index
    apps = App.all
    paginate json: apps, per_page: 3

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
end
