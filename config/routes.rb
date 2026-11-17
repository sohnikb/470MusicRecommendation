Rails.application.routes.draw do
  get 'home/index'
  root "home#index"
  get "music", to: "music_search#index", as: :music_search
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Defines the root path route ("/")
  # root "articles#index"
end
