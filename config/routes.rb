Rails.application.routes.draw do
  match '/api/apps',        to: 'apps#index', via: 'get', defaults: {format: 'json'}
  match '/api/plugins',     to: 'apps#index', via: 'get', defaults: {format: 'json'}
  match '/api/plugins/:id', to: 'apps#show',  via: 'get', defaults: {format: 'json'}
end
