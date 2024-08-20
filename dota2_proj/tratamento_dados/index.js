const fs = require('node:fs');



const {headers: HEADERS, rows: ROWS} = getFullData();

const HEADERS_TO_REMOVE = getHeadersToRemove();

const NEW_HEADERS = HEADERS.filter((header) => !HEADERS_TO_REMOVE.includes(header));

const NEW_ROWS = ROWS.map((row) => {
    const newRow = [];
    for (let i = 0; i < row.length; i++) {
        if (!HEADERS_TO_REMOVE.includes(HEADERS[i]))
            newRow.push(row[i]);
    }
    return newRow
});

let NEW_FILE = NEW_HEADERS.join(',')
NEW_FILE += '\n';
NEW_FILE += NEW_ROWS.map((row) => row.join(',')).join('\n');

try {
    fs.writeFileSync('./output.csv', NEW_FILE);
} catch (err) {
    console.error(err);
    throw err;
}

// FETCH FUNCTIONS

function getFullData() {
    let data;
    
    try {
        data = fs.readFileSync('./matches_preview.csv', 'utf8');
    } catch (err) {
        console.error(err);
        throw err;
    }

    const headers = data.split('\n')[0].split(',');
    const rows = data.split('\n').slice(1).map((row) => row.split(','));

    return {headers, rows};
}

function getHeadersToRemove() {
    let headersToRemoveFile;
    
    try {
        headersToRemoveFile = fs.readFileSync('./headers_to_remove.txt', 'utf8');
    } catch (err) {
        console.error(err);
        throw err;
    }

    return headersToRemoveFile.split('\n').map((headerWithCommentLine) => headerWithCommentLine.split(' //')[0]);
}
