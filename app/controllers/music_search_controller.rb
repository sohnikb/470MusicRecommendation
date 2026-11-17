class MusicSearchController < ApplicationController
    SAMPLE_SONGS = [
        {
            title: "Rechazame",
            artist: "Prince Royce",
            genre: "Latino Pop"
        },
        {
            title: "stupid song",
            artist: "Olivia Rodrigo",
            genre: "Pop Rock"
        },
        {
            title: "Heather",
            artist: "Conan Gray",
            genre: "Indie Pop"
        }
    ].freeze

    def index
        @query = params[:q].to_s.strip
        @recommendations = @query.present? ? SAMPLE_SONGS : []
    end
end