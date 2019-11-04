import './App.css';
import React, {Component} from "react";
import ReactDOM from 'react-dom';
import { Player } from 'video-react';
import '../node_modules/video-react/dist/video-react.css';
import videojs from '../node_modules/video.js'
import 'video.js/dist/video-js.css'

var numOfCamera = 0;
var date = "";
var time = "";

class App extends Component {
    constructor(){
        super();
        this.state = {
            numCamera: 0,
            date: ""
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const {name, value, type} = event.target;

        if(type === "date"){
            var date1 = new Date(value);
            var now = new Date();
            console.log(now);
            console.log(date1);

            if(date1 - now <= 0 &&
                date1 - (now.setMonth(now.getMonth() - 2)) > 0){

                console.log("GOOD date");
                this.setState({
                    [name]: value
                });
                console.log(this.state)

            }
        }
        else
        {
            this.setState({
                [name]: value
            })
        }
    }

    handleSubmit(event){
        event.preventDefault();

        if(this.state.numCamera !== 0 && this.state.date !== ""){
            numOfCamera = this.state.numCamera;
            date = this.state.date;
            console.log("HUUUI2");
            ReactDOM.render(<ChooseTime />, document.getElementById('root'))
        }
    }

    render() {
        return (
            <div className="App">
                <form onSubmit={this.handleSubmit}>
                    <select
                        value={this.state.numCamera}
                        name="numCamera"
                        onChange={this.handleChange}
                        required
                    >
                        <option value={0}>Please enter number of camera</option>
                        <option value={1}>Stiralka</option>
                        <option value={2}>KDS</option>
                    </select>
                    <br />

                    <p>Choose date from last two month</p>

                    <input
                        type="date"
                        name="date"
                        value={this.state.date}
                        onChange={this.handleChange}
                        placeholder="1999-01-21"
                        required
                    />
                    <br />
                    <br />

                    <button>Submit</button>
                </form>
            </div>
        );
    }
}

class ChooseTime extends Component{

    constructor(){
        super();
        this.state = {
            listOfVideoName: [],
            selectedVideo: ""
        };

        this.handleChange = this.handleChange.bind(this);

        fetch("http://localhost:8080/testcloud/" + numOfCamera + "/" + date + "/")
            .then(response => response.json())
            .then(response =>{
                console.log("Response:");
                console.log(response);

                let videoFromApi = response.map(videoName => {
                    var reg = /(record_[0-9-]*_([0-9-]*)\.avi)/i;
                    return [videoName.match(reg)[2], videoName.match(reg)[2]]
                });
                console.log(this);
                // this.setState({ listOfVideoName: [{value: '', display: '(Select video time)'}].concat(videoFromApi) });
                this.setState({ listOfVideoName: [...videoFromApi, ['', '-Select video time-'] ] });
                //this.setState({ listOfVideoName: [...this.state.listOfVideoName, {value: '', display: '(Select video time)'} ] });

                console.log("List:");
                console.log(this.state.listOfVideoName);
                console.log("Loading: done");

            });

        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        var {value} = event.target;

        this.setState({
            selectedVideo: value
        })
    }

    handleSubmit(event){
        event.preventDefault();

        console.log(this);

        if(this.state.selectedVideo !== ''){
            time = this.state.selectedVideo;
            ReactDOM.render(<Video />, document.getElementById("root"));
        }
    }

    render() {
        console.log(this);

        return (
            <div>
                <h2>Ret: {this.state.listOfVideoName}</h2>

                <form onSubmit={this.handleSubmit}>
                    <select value={this.state.selectedVideo}
                            onChange={this.handleChange}
                            required>
                        {this.state.listOfVideoName.map((video) =>
                            <option key={video[0]} value={video[0]}>{video[1]}</option>)}
                    </select>
                    <br />

                    <button>Submit</button>
                </form>
            </div>
        )
    }
}

class Video extends Component{
    constructor(props, context){
        super(props, context);

        this.state = {
            playerSource: "http://localhost:8080/video/" + numOfCamera +
                "/" + date + "/" + time + "/"
        };
    }

    render() {
        /*return(
            {/!*<div>
                <Player
                    ref={player => {
                        this.player = player;
                    }}
                >
                    <source src={this.state.playerSource} />
                </Player>
            </div>*!/}

        )*/
        const videoJsOptions = {
            autoplay: true,
            controls: true,
            sources: [{
                src: this.state.playerSource,
                type: 'video/mp4'
            }]
        };

        return <VideoPlayer { ...videoJsOptions } />
    }
}

class VideoPlayer extends React.Component {
    componentDidMount() {
        // instantiate Video.js
        this.player = videojs(this.videoNode, this.props, function onPlayerReady() {
            console.log('onPlayerReady', this)
        });
    }

    // destroy player on unmount
    componentWillUnmount() {
        if (this.player) {
            this.player.dispose()
        }
    }

    render() {
        return (
            <div>
                <div data-vjs-player>
                    <video ref={ node => this.videoNode = node } className="video-js"></video>
                </div>
            </div>
        )
    }
}

export default App;
